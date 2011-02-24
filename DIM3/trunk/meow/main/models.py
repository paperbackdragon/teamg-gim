from django.db import models
from django.contrib.auth.models import User
from time import mktime

"""
Describes who each user is following
"""
class Follow(models.Model):
    user = models.ForeignKey(User, related_name = 'UserIs', verbose_name = 'User')
    follows = models.ForeignKey(User, related_name = 'Following', verbose_name = 'Follows')

    class Meta:
        unique_together = (('user', 'follows'),)

    def __unicode__(self):
        return u'%s --> %s' % (self.user.get_full_name(),  self.follows.get_full_name())



"""
Describes a Tag which can be used within a meow
"""
class Tag(models.Model):
    tag = models.CharField(max_length = 100, verbose_name = 'Tag')

    def __unicode__(self):
        return u'%s' % (self.tag)


"""
Describes a meow
"""
class Meow(models.Model):
    by = models.ForeignKey(User, verbose_name = 'Posted By')
    meow = models.TextField(verbose_name = 'Meow')
    time = models.DateTimeField(auto_now_add = True, verbose_name = 'Time of Meow')
    media_url = models.URLField(verify_exists = True, verbose_name = 'URL of Media')
    media_type = models.CharField(max_length = 1, choices = (('V', 'Video'), ('I', 'Image')), verbose_name = 'Type of Media')
    mentions = models.ManyToManyField(User, related_name = 'isMentioned', verbose_name = 'Users Mentioned')
    tags = models.ManyToManyField(Tag, verbose_name = 'Tags')

    def epoch_time(self):
        return mktime(self.time.timetuple())

