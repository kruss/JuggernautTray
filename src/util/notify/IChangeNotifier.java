package util.notify;

public interface IChangeNotifier {

	public void addListener(IChangeListener listener);
	public void removeListener(IChangeListener listener);
	public void notifyListeners();
}
