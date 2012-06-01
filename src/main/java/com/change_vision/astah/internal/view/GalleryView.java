package com.change_vision.astah.internal.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

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
		InputStream stream = getClass().getClassLoader().getResourceAsStream("imagelist.txt");
		Reader ir = new InputStreamReader(stream);
		BufferedReader reader = new BufferedReader(ir);
		ImagePathLoader loader = new ImagePathLoader();
		try {
			groups = loader.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		add(pane, BorderLayout.CENTER);
		pane.setLeftComponent(createTaskPaneContainer());
		pane.setRightComponent(createImagePaneContainer());
		pane.setDividerLocation(300);
		String path = groups.get(0).getPaths().get(0);
		setImage(path);
	}

	private JScrollPane createImagePaneContainer() {
		image = new JXImagePanel();
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new BorderLayout());
		imagePanel.add(image);
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