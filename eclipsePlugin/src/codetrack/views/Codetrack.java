package codetrack.views;

import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.part.ViewPart;

import codetrack.listeners.Listeners;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class Codetrack extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "codetrack.views.Codetrack";

	@Inject
	IWorkbench workbench;

	private Composite mView;

	public Codetrack() {
		super();
		this.initListeners();
	}

	private void initListeners() {
		try {
			new Listeners();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFocus() {
		mView.setFocus();
	}

	public void createPartControl(Composite parentView) {
		mView = new MainView(parentView, 0);

//		System.out.println("------");
//		IEditorPart editorPart =  workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
//
//		 ITextEditor editor = (ITextEditor) editorPart;
//		 IDocument document = editor.getDocumentProvider().getDocument(
//				 editor.getEditorInput());
//			System.out.println(document.getNumberOfLines());
		/*
		 * IResourceChangeListener listener = new IResourceChangeListener() {
		 * 
		 * @Override public void resourceChanged(IResourceChangeEvent arg0) {
		 * System.out.println("Text changed"); } };
		 * ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
		 */
		/*
		 * document.addDocumentListener(new IDocumentListener() {
		 * 
		 * @Override public void documentChanged(DocumentEvent event) { // Do something
		 * System.out.println("documentChanged"); System.out.println(event); }
		 * 
		 * @Override public void documentAboutToBeChanged(DocumentEvent event) { //
		 * About to do something
		 * System.out.println("---- -- -about to be changed----- --- "); } });
		 */
		/*
		 * if (document != null) { IRegion lineInfo = null; try { int lineNumber; //
		 * line count internaly starts with 0, and not with 1 like in // GUI lineInfo =
		 * document.getLineInformation(0); System.out.println(lineInfo); } catch
		 * (BadLocationException e) { // ignored because line number may not really
		 * exist in document, // we guess this... } if (lineInfo != null) {
		 * editor.selectAndReveal(lineInfo.getOffset(), lineInfo.getLength()); } }
		 */

		System.out.println("------");
	}

}
