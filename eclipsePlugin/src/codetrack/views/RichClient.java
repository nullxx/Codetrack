package codetrack.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import org.eclipse.swt.widgets.Composite;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RichClient {
	private BrowserListener listener;
	private Browser browser;

	public RichClient(Composite parent, String url) {

		browser = new Browser(parent, SWT.NONE);
		browser.setUrl(url);
		BrowserThreadBridge callbackRunner = new BrowserThreadBridge() {
			@Override
			public Object run(BrowserListenerResponse bListenerResponse) {
				return RichClient.this.notifyCallbackRecieved(bListenerResponse);
			}

			@Override
			public void run() {
				return;
			}
		};
		new CallbackDebugFunction(browser, "jsToSwtCallback", callbackRunner);
	}

	public void dispose() {
		browser.dispose();
	}

	public boolean send(String something) {
		return browser.execute(something);
	}

	static class CallbackDebugFunction extends BrowserFunction {
		private BrowserThreadBridge callbackRunner;

		public CallbackDebugFunction(final Browser browser, final String name, BrowserThreadBridge callbackRunner) {
			super(browser, name);
			this.callbackRunner = callbackRunner;
		}

		@Override
		public Object function(final Object[] arguments) {
			if (arguments.length >= 1) {
				String type = (String) arguments[0];
				JsonObject data = null;

				if (arguments.length >= 2) {
					data = (JsonObject) new JsonParser().parse((String) arguments[1]);
				}

				BrowserListenerResponse browserListener = new BrowserListenerResponse(type, data);
				return this.callbackRunner.run(browserListener);
			}
			return false;
		}

	}

	public void addListener(BrowserListener toAdd) {
		listener = toAdd;
	}

	public Object notifyCallbackRecieved(BrowserListenerResponse bListenerResponse) {
		return listener.bCallback(bListenerResponse);

	}

}