package com.piinfo.db;

import com.piinfo.Application;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.core.NestedCheckedException;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.ConnectException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
public class SpringLiquibaseTest {
	@Rule
	public final OutputCapture output = new OutputCapture();

	public String masterchangelog = "classpath:db/changelog-master.xml:";
	public String changelog1 = " classpath:db/changelogs/1.xml::";

	@Value("${author}")
	public String author;

	@Test
	public void testDefaultSettings () throws Exception {
		try {
			Application.main(new String[]{"--server.port=0"});
		} catch (IllegalStateException ex) {
			if (serverNotRunning(ex)) {
				return;
			}
		}
		assertThat(this.output.toString())
				.contains("Successfully acquired change log lock")
				.contains("Creating database history "
						+ "table with name: PUBLIC.DATABASECHANGELOG")
				.contains("Reading from PUBLIC.DATABASECHANGELOG")
				.contains(masterchangelog + changelog1 + "1::" + author + ": " +
						"Table User created")
				.contains(masterchangelog + changelog1 + "1::" + author + ":"
						+ " ChangeSet" + changelog1 + "1::" + author
						+ " ran successfully")
				.contains(masterchangelog + changelog1 + "2::" + author + ": " +
						"Index User_username_index created")
				.contains(masterchangelog + changelog1 + "2::" + author + ":"
						+ " ChangeSet" + changelog1 + "2::" + author
						+ " ran successfully")
				.contains(masterchangelog + changelog1 + "3::" + author + ": " +
						"New row inserted into User")
				.contains(masterchangelog + changelog1 + "3::" + author + ":"
						+ " ChangeSet" + changelog1 + "3::" + author
						+ " ran successfully")
				.contains("Successfully released change log lock");
	}

	@SuppressWarnings("serial")
	private boolean serverNotRunning (IllegalStateException ex) {
		NestedCheckedException nested = new NestedCheckedException("failed", ex) {
		};
		if (nested.contains(ConnectException.class)) {
			Throwable root = nested.getRootCause();
			return root.getMessage().contains("Connection refused");
		}
		return false;
	}
}
