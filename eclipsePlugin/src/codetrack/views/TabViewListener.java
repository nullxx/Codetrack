package codetrack.views;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class TabViewListener extends TabItem {
	private ArrayList<TabFocusListener> listeners = new ArrayList<TabFocusListener>();

	public TabViewListener(TabFolder parent, int style, String title) {
		super(parent, style);
		this.setText(title);
		this.getParent().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.item.equals(TabViewListener.this)) {
					TabViewListener.this.toggleRefresh();
				}
			}
		});

	}

	private void toggleRefresh() {
		for (TabFocusListener hl : listeners)
			hl.refresh();
	}

	public void addListener(TabFocusListener toAdd) {
		listeners.add(toAdd);
	}

	public void refreshView() {
		this.getParent().layout(true, true);
	}
}
