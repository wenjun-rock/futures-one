package fwj.futures.data.launch;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractBaseLaunch {
	
	protected Logger log = Logger.getLogger(this.getClass());

	protected static void launch(Class<? extends AbstractBaseLaunch> runClass) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		AbstractBaseLaunch launch = context.getBean(runClass);
		try {
			launch.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		context.close();
		context.destroy();
	}

	abstract protected void execute() throws Exception;

}
