/**
 * 
 */
package codetrack.listeners;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * @author nullx
 *
 */
public class Listeners {

	ResourcesPlugin resPlugin;
	
	
	public Listeners() throws Exception {
		this.init();
	}
	
	private void init() {
		this.onSave();
	}
	


	private void onSave() {
		System.out.println("he llegado hasta aqui");
		IResourceChangeListener listener = new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent e) {
				if (e.getType() == IResourceChangeEvent.POST_CHANGE) {
					// postchange will be triggered more than one time after save
					System.out.println("POST CHANGE");
				}
			}
		   };
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
	}

}
