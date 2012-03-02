/**
 * 
 */
package com.monstersoftwarellc.graphtastic;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Using an empty abstract class that all Application Context Aware Tests can
 * extend to always use the same context.  If you don't need access to the 
 * ApplicationContext then you don't need to worry about adding any annotations.
 * @author nicholas
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="file:src/main/webapp/WEB-INF/springTest/test-context.xml")
public abstract class TestApplicationContext {

}
