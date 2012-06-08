package com.change_vision.astah.internal.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXImagePanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

@SuppressWarnings("serial")
public class GalleryView extends JPanel implements IPluginExtraTabView,
		ProjectEventListener {
	
	class DragScrollListener extends MouseAdapter {
	    private final Cursor defCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	    private final Cursor hndCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	    private final Point pp = new Point();
	    @Override public void mouseDragged(MouseEvent e) {
	        final JComponent jc = (JComponent)e.getSource();
	        Container c = jc.getParent();
	        if(c instanceof JViewport) {
	            JViewport vport = (JViewport)c;
	            Point cp = SwingUtilities.convertPoint(jc,e.getPoint(),vport);
	            Point vp = vport.getViewPosition();
	            vp.translate(pp.x-cp.x, pp.y-cp.y);
	            jc.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
	            pp.setLocation(cp);
	        }
	    }
	    @Override public void mousePressed(MouseEvent e) {
	        JComponent jc = (JComponent)e.getSource();
	        Container c = jc.getParent();
	        if(c instanceof JViewport) {
	            jc.setCursor(hndCursor);
	            JViewport vport = (JViewport)c;
	            Point cp = SwingUtilities.convertPoint(jc,e.getPoint(),vport);
	            pp.setLocation(cp);
	        }
	    }
	    @Override public void mouseReleased(MouseEvent e) {
	        ((JComponent)e.getSource()).setCursor(defCursor);
	    }
	}

	private final class OpenGalleryWindowAction extends AbstractAction {
		
		public OpenGalleryWindowAction(){
			URL url = getClass().getClassLoader().getResource("icons/open.png");
			putValue(Action.SMALL_ICON, new ImageIcon(url));
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame frame = new JFrame("Gallery");
			frame.setSize(800, 500);
			frame.setLocationRelativeTo(null);
			frame.add(new GalleryView(false));
			frame.setVisible(true);
		}
	}

	public class OpenImageAction extends AbstractAction {

		private String path;

		public OpenImageAction(String path) {
			putValue(Action.NAME, getDiagName(path));
			this.path = path;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setImage(path);
			imageScrollPane.getViewport().setViewPosition(new Point(0,0));
		}

	}

	private List<ImagePathGroup> groups;
	private JXImagePanel image;
	private JScrollPane imageScrollPane;

	public GalleryView() {
		this(true);
	}

	public GalleryView(boolean showToolbar) {
		InputStream stream = getClass().getClassLoader().getResourceAsStream("imagelist.txt");
		Reader ir = new InputStreamReader(stream);
		BufferedReader reader = new BufferedReader(ir);
		ImagePathLoader loader = new ImagePathLoader();
		try {
			groups = loader.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		initComponents(showToolbar);
	}

	private void initComponents(boolean showToolbar) {
		setLayout(new BorderLayout());
		if(showToolbar){
			JToolBar bar = createToolBar();
			add(bar, BorderLayout.NORTH);
		}
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		add(pane, BorderLayout.CENTER);
		pane.setLeftComponent(createTaskPaneContainer());
		pane.setRightComponent(createImagePaneContainer());
		pane.setDividerLocation(300);
		String path = groups.get(0).getPaths().get(0);
		setImage(path);
	}

	private JToolBar createToolBar() {
		JToolBar bar = new JToolBar("gallery_toolbar");
		bar.add(Box.createGlue());
		JButton openButton = new JButton();
		openButton.setRequestFocusEnabled(false);
		openButton.setAction(new OpenGalleryWindowAction());
		bar.add(openButton);
		return bar;
	}

	private JScrollPane createImagePaneContainer() {
		image = new JXImagePanel();
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new BorderLayout());
		imagePanel.add(image);
		DragScrollListener listener = new DragScrollListener();
		imagePanel.addMouseMotionListener(listener);
		imagePanel.addMouseListener(listener);
		imageScrollPane = new JScrollPane(imagePanel);
		return imageScrollPane;
	}

	private Container createTaskPaneContainer() {
		
		JXTaskPaneContainer pane = new JXTaskPaneContainer();
		
		boolean first = true;
		for (ImagePathGroup group : groups) {
			JXTaskPane taskPane = createTaskPane(group);
			if(!first)taskPane.setCollapsed(true);
			pane.add(taskPane);
			first = false;
		}
		
		JScrollPane scroll = new JScrollPane(pane);
		return scroll;
	}
	
	private JXTaskPane createTaskPane(ImagePathGroup group) {
		JXTaskPane pane = new JXTaskPane();
		pane.setTitle(group.getName());
		for (String path : group.getPaths()) {
			pane.add(new OpenImageAction(path));
		}
		return pane;
	}

	
	private void setImage(String path) {
		ClassLoader loader = getClass().getClassLoader();
		String imagePath = imagePath(path);
		URL stream = loader.getResource(imagePath);
		try {
			image.setImage(ImageIO.read(stream));
		} catch (IOException e) {
		}
		image.setBorder(BorderFactory.createTitledBorder(getDiagName(path)));
		image.revalidate();
	}


	private String getDiagName(String path) {
		return path.substring(path.lastIndexOf("/") + 1);
	}

	private String imagePath(String imageName) {
		return "images/" + imageName;
	}

	@Override
	public void projectChanged(ProjectEvent e) {
	}

	@Override
	public void projectClosed(ProjectEvent e) {
	}

	@Override
	public void projectOpened(ProjectEvent e) {
	}

	@Override
	public void addSelectionListener(ISelectionListener listener) {
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public String getDescription() {
		return "Gallery";
	}

	@Override
	public String getTitle() {
		return "Gallery";
	}

	public void activated() {

	}

	public void deactivated() {

	}
	
}