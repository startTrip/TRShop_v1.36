package shop.trqq.com.ui.Base;

import android.os.Handler;
import android.os.Message;

import java.util.Vector;

/**
 * http://stackoverflow.com/questions/8040280/how-to-handle-handler-messages-when-activity-fragment-is-paused
 * <p/>
 * Message Handler class that supports buffering up of messages when the
 * activity is paused i.e. in the background.
 */
public abstract class PauseHandler extends Handler {

    /**
     * Message Queue Buffer
     */
    final Vector<Message> messageQueueBuffer = new Vector<Message>();

    /**
     * Flag indicating the pause state
     */
    private boolean paused;

    /**
     * Resume the handler
     */
    final public void resume() {
        paused = false;
        //当messageQueueBuffer队列有消息 发送这个消息并移除
        while (messageQueueBuffer.size() > 0) {
            final Message msg = messageQueueBuffer.elementAt(0);
            messageQueueBuffer.removeElementAt(0);
            sendMessage(msg);
        }
    }

    /**
     * Pause the handler
     */
    final public void pause() {
        paused = true;
    }

    /**
     * Notification that the message is about to be stored as the activity is
     * paused. If not handled the message will be saved and replayed when the
     * activity resumes.
     *
     * @param message the message which optional can be handled
     * @return true if the message is to be stored
     */
    protected abstract boolean storeMessage(Message message);

    /**
     * Notification message to be processed. This will either be directly from
     * handleMessage or played back from a saved message when the activity was
     * paused.
     *
     * @param message the message to be handled
     */
    protected abstract void processMessage(Message message);

    /**
     * {@inheritDoc}
     */
    @Override
    final public void handleMessage(Message msg) {
        //当消息为paused暂停拷贝消息到messageQueueBuffer队列，等到onresume执行
        if (paused) {
            if (storeMessage(msg)) {
                Message msgCopy = new Message();
                msgCopy.copyFrom(msg);
                messageQueueBuffer.add(msgCopy);
            }
        } else {
            processMessage(msg);
        }
    }
}
