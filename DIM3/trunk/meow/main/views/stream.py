from django.http import HttpResponse
from django.shortcuts import render_to_response
from django.core.context_processors import csrf
from django.utils import simplejson
from django.contrib.auth.decorators import login_required
from django.template.loader import get_template
from django.template import RequestContext
from django.db.models import Q

from meow.main.models import Meow, Follow

from datetime import datetime

"""
Generate the JSON for a request
"""
def JSON(request, start = '0', limit = '500'):
    start = int(start)
    limit = int(limit)

    if limit <= 0:
        limit = 500

    # Get the users that this user is following
    following = [f.follows for f in Follow.objects.filter(user=request.user)]

    # Get the meows by the current user and the users they follow
    meows = Meow.objects.filter(pk__gt=start).filter(Q(by__in=following) | Q(by=request.user) | Q(meow__startswith='@' + request.user.username))[:limit]

    # Construct the data
    data = []
    for m in meows:
        d = {'uid': m.by.pk,
             'mid': m.pk,
             'username': m.by.username,
             'firstname': m.by.first_name,
             'lastname': m.by.last_name,
             'time': m.epoch_time(),
             'message': m.meow }
        if m.media_url:
            d['media'] = { 'type': m.media_type, 'url': m.media_url }

        data += [d]

    return HttpResponse(simplejson.dumps(data), mimetype='application/javascript')


"""
Show the currently logged in user their global stream
"""
@login_required
def main(request):
    data = {}

    # Get the users that this user is following
    data['following'] = [f.follows for f in Follow.objects.filter(user=request.user)[:16]]

    # Get the users following this user
    data['follows'] = [f.user for f in Follow.objects.filter(follows=request.user)[:16]]

    # Their last meow
    data['last_meow'] = Meow.objects.filter(by=request.user)[0]

    return render_to_response('main.html', data, context_instance=RequestContext(request))


"""
Show the profile page of the given user
"""
def user(request, user):
    return HttpResponse(user)
