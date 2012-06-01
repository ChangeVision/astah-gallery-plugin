package com.change_vision.astah.internal.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

@SuppressWarnings("serial")
public class GalleryView extends JPanel implements IPluginExtraTabView,
		ProjectEventListener {

	public GalleryView() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		add(createLabelPane(), BorderLayout.CENTER);
		addProjectEventListener();
	}

	private void addProjectEventListener() {
		try {
			ProjectAccessor projectAccessor = ProjectAccessorFactory
					.getProjectAccessor();
			projectAccessor.addProjectEventListener(this);
		} catch (ClassNotFoundException e) {
			e.getMessage();
		}
	}

	private Container createLabelPane() {
		Icon icon = createIcon();
		JLabel label = new JLabel(icon);
		JScrollPane pane = new JScrollPane(label);
		return pane;
	}

	private Icon createIcon() {
		InputStream stream = getClass().getClassLoader().getResourceAsStream("images/0. Withdrawal service of saving account[Context].png");
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(stream);
		} catch (IOException e) {
		}
		Icon icon = new ImageIcon(bufferedImage);
		return icon;
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