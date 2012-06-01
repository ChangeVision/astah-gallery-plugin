package com.change_vision.astah.internal.view;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ImagePathLoaderTest {
	
	ImagePathLoader loader;

	@Before
	public void before() throws Exception {

		loader = new ImagePathLoader();
		
	}

	@Test(expected=IllegalArgumentException.class)
	public void nullValueTest() throws IOException {
		loader.load(null);
	}
	
	@Test
	public void emptyValue() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader(""));
		List<ImagePathGroup> groups = loader.load(reader);

		assertThat(groups,is(notNullValue()));
		assertThat(groups.size(),is(0));
	}
	
	@Test
	public void hasOneGroup() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("[GroupA]"));
		List<ImagePathGroup> groups = loader.load(reader);

		assertThat(groups,is(notNullValue()));
		assertThat(groups.size(),is(1));
		ImagePathGroup group = groups.get(0);
		assertThat(group.getName(),is("GroupA"));
		assertThat(group.getPaths().size(),is(0));
	}
	
	@Test
	public void hasOneGroupAndAPath() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("[GroupA]\nhoge.png"));
		List<ImagePathGroup> groups = loader.load(reader);

		assertThat(groups,is(notNullValue()));
		assertThat(groups.size(),is(1));
		ImagePathGroup group = groups.get(0);
		assertThat(group.getName(),is("GroupA"));
		assertThat(group.getPaths().size(),is(1));
		assertThat(group.getPaths().get(0),is("hoge.png"));
	}
	
	@Test
	public void hasOneGroupAndPaths() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("[GroupA]\nhoge.png\nfuga.png"));
		List<ImagePathGroup> groups = loader.load(reader);

		assertThat(groups,is(notNullValue()));
		assertThat(groups.size(),is(1));
		ImagePathGroup group = groups.get(0);
		assertThat(group.getName(),is("GroupA"));
		assertThat(group.getPaths().size(),is(2));
		assertThat(group.getPaths().get(0),is("hoge.png"));
		assertThat(group.getPaths().get(1),is("fuga.png"));
	}
	
	@Test
	public void hasTwoGroups() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("[GroupA]\n[GroupB]"));
		List<ImagePathGroup> groups = loader.load(reader);

		assertThat(groups,is(notNullValue()));
		assertThat(groups.size(),is(2));
		ImagePathGroup group = groups.get(0);
		assertThat(group.getName(),is("GroupA"));
		group = groups.get(1);
		assertThat(group.getName(),is("GroupB"));
	}

	@Test
	public void hasTwoGroupsAndEachHasAPath() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("[GroupA]\nhoge.png\n[GroupB]\nfuga.png"));
		List<ImagePathGroup> groups = loader.load(reader);

		assertThat(groups,is(notNullValue()));
		assertThat(groups.size(),is(2));
		ImagePathGroup group = groups.get(0);
		assertThat(group.getName(),is("GroupA"));
		assertThat(group.getPaths().size(),is(1));
		assertThat(group.getPaths().get(0),is("hoge.png"));
				
		group = groups.get(1);
		assertThat(group.getName(),is("GroupB"));
		assertThat(group.getPaths().size(),is(1));
		assertThat(group.getPaths().get(0),is("fuga.png"));
	}

	
	@Test
	public void illegalGroup() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("a[GroupA]"));
		List<ImagePathGroup> groups = loader.load(reader);

		assertThat(groups,is(notNullValue()));
		assertThat(groups.size(),is(0));
	}



}
