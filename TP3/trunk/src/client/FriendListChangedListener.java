package client;

public interface FriendListChangedListener {
	public void friendAdded(User user);
	public void friendRemove(User user);
	public void stateChanged();
}
