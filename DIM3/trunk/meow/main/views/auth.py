from django.shortcuts import render_to_response

def login(Request):
    return render_to_response('main.html')

def logout(Request):
    pass
