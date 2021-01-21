package codetrack.views;

public interface BrowserListener {
	/**
	 * Triggered from browser
	 * @param browserListenerResponse
	 * @return
	 */
	Object bCallback(BrowserListenerResponse browserListenerResponse);
}
