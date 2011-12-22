package util.notify;

import static org.junit.Assert.*;

import org.junit.Test;

import util.logger.LoggerMock;

public class ChangeNotifierTest {
	
	@Test public void testChangeNotifier() {
		
		ChangeableObjectMock object1 = new ChangeableObjectMock();
		ChangeableObjectMock object2 = new ChangeableObjectMock();
		ChangeListenerMock listener1 = new ChangeListenerMock();
		ChangeListenerMock listener2 = new ChangeListenerMock();
		
		object1.notifier.addListener(listener1);
		object1.notifier.addListener(listener2);
		object2.notifier.addListener(listener1);
		object2.notifier.addListener(listener2);
		
		object1.changed();
		assertEquals(1, listener1.changedCalled);
		assertEquals(object1, listener1.changedObject);
		assertEquals(1, listener2.changedCalled);
		assertEquals(object1, listener2.changedObject);
		
		object2.changed();
		assertEquals(2, listener1.changedCalled);
		assertEquals(object2, listener1.changedObject);
		assertEquals(2, listener2.changedCalled);
		assertEquals(object2, listener2.changedObject);
		
		listener1.reset();
		listener2.reset();
		object1.notifier.removeListener(listener2);
		object2.notifier.removeListener(listener1);
		
		for(int i=0; i<10; i++){
			
			object1.changed();
			assertEquals(i+1, listener1.changedCalled);
			assertEquals(object1, listener1.changedObject);
			
			object2.changed();
			assertEquals(i+1, listener2.changedCalled);
			assertEquals(object2, listener2.changedObject);;
		}
	}

	private class ChangeableObjectMock {
		
		public IChangeNotifier notifier;
		
		public ChangeableObjectMock(){
			notifier = new ChangeNotifier(new LoggerMock(), this);
		}
		
		public void changed(){
			notifier.notifyListeners();
		}
	}
	
	private class ChangeListenerMock implements IChangeListener {

		public int changedCalled;
		public Object changedObject;
		
		public ChangeListenerMock() {
			reset();
		}
		
		private void reset() {
			changedCalled = 0;
			changedObject = null;
		}

		@Override
		public void changed(Object object) {
			changedCalled++;
			changedObject = object;
		}
	}
}
