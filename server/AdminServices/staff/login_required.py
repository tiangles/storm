from django.shortcuts import render


def login_required(func):
    def _deco(request):
        if hasattr(request, 'user'):
            user = request.user
            if user is not None and user.is_active and user.is_authenticated():
                return func(request)
        return render(request, 'login.html', {})

    return _deco
