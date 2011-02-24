from django.http import HttpResponse
from django.shortcuts import render_to_response

from django.template.loader import get_template
from django.template import Context

from meow.main.models import Meow

"""
Generate the JSO for a request
"""
def JSON(Request, start = '0', limit = '100'):
    return render_to_response('base.json', {'Meows': Meow.objects.all()})


def main(Request):
    return render_to_response('main.html', {})


def user(Request, user):
    return HttpResponse(user)
