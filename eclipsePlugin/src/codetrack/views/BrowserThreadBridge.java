package codetrack.views;

public interface BrowserThreadBridge extends Runnable {

	public Object run(BrowserListenerResponse bListenerResponse);

}
