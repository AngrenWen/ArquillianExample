package tv.beenius.greeter.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ApplyScriptAfter;
import org.jboss.arquillian.persistence.ApplyScriptBefore;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import tv.beenius.greeter.entities.Hello;

@RunWith(Arquillian.class)
@ApplyScriptBefore("create.sql")
@ApplyScriptAfter("drop.sql")
@Cleanup(phase = TestExecutionPhase.NONE)
public class HelloWorldServiceTest {

	@PersistenceContext(name = "hello-persistence-unit")
	private EntityManager manager;

	@Deployment
	public static JavaArchive createDeployment() {

		JavaArchive jar = ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addClasses(Hello.class, HelloDao.class)
				.addAsResource("META-INF/persistence-test.xml",
						"META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

		return jar;
	}

	@Test
	public void testEntityManagerNotNull() {
		Assert.assertNotNull(manager);
	}

	@Test
	public void testCountHelloRows() {
		HelloDao dao = new HelloDao(manager);
		Assert.assertEquals(2, dao.countRows());
	}
}
