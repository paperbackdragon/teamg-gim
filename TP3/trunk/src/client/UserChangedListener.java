package client;

public interface UserChangedListener {
	
	public void changed();
	
	public void nicknameChanged();

	public void statusChanged();

	public void personalMessageChanged();

	public void DisplayPicChnaged();
}