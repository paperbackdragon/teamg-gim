from django.http import HttpResponse

from django.contrib.auth.decorators import login_required

from meow.main.models import Meow

@login_required
def add_meow(request):
    errors = []

    if not request.is_ajax():
        # Send them off to somewhere. This shouldn't happen
        pass

    if request.method == 'POST':
        if not request.POST.get('text', ''):
            errors.append('No message entered')

    text = request.POST.get('text', '').replace('\n', ' ').strip()
    if len(text) == 0:
        errors.appent("No message")

    if not errors:
        meow = Meow(by=request.user, meow=text)
        meow.save()

    return HttpResponse("Hello")
