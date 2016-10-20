package shop.trqq.com.widget;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 *  �Ի����װ��
 * Created by Weiss on 2016/3/8.
 */
public class DialogTool {
    /**
     * ������ͨ�Ի���
     *
     * @param ctx ������ ����
     * @param iconId ͼ�꣬�磺R.drawable.icon ����
     * @param title ���� ����
     * @param message ��ʾ���� ����
     * @param btnName ��ť���� ����
     * @param listener ����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
     * @return
     */
    public static Dialog createNormalDialog(Context ctx,
                                            int iconId,
                                            String title,
                                            String message,
                                            String btnName,
                                            DialogInterface.OnClickListener listener) {
        Dialog dialog=null;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        // ���öԻ����ͼ��
        builder.setIcon(iconId);
        // ���öԻ���ı���
        builder.setTitle(title);
        // ���öԻ������ʾ����
        builder.setMessage(message);
        // ��Ӱ�ť��android.content.DialogInterface.OnClickListener.OnClickListener
        builder.setPositiveButton(btnName, listener);
        // ����һ����ͨ�Ի���
        dialog = builder.create();
        return dialog;
    }

    /**
     * ����MD��ͨ�Ի���2
     *
     * @param ctx ������ ����
     * @param message ��ʾ���� ����
     * @param btnName1 ��ť���� ����
     * @param listener1 ����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
     * @return
     */
    public static AlertDialog createNormalDialog2(Context ctx,
                                            String message,
                                            String btnName1,
                                            String btnName2,
                                            DialogInterface.OnClickListener listener1,
                                            DialogInterface.OnClickListener listener2) {
        AlertDialog mAlertDialog=null;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        // ���öԻ������ʾ����
        builder.setMessage(message);
        builder.setNegativeButton(btnName1, listener1);
        // ��Ӱ�ť��android.content.DialogInterface.OnClickListener.OnClickListener
        builder.setPositiveButton(btnName2, listener2);
        // ����һ����ͨ�Ի���
        mAlertDialog = builder.create();
        return mAlertDialog;
    }


    /**
     * �����б�Ի���
     *
     * @param ctx ������ ����
     * @param iconId ͼ�꣬�磺R.drawable.icon ����
     * @param title ���� ����
     * @param itemsId �ַ���������Դid ����
     * @param listener ����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
     * @return
     */
    public static Dialog createListDialog(Context ctx,
                                          int iconId,
                                          String title,
                                          int itemsId,
                                          DialogInterface.OnClickListener listener) {
        Dialog dialog=null;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        // ���öԻ����ͼ��
        builder.setIcon(iconId);
        // ���öԻ���ı���
        builder.setTitle(title);
        // ��Ӱ�ť��android.content.DialogInterface.OnClickListener.OnClickListener
        builder.setItems(itemsId, listener);
        // ����һ���б�Ի���
        dialog = builder.create();
        return dialog;
    }

    /**
     * ������ѡ��ť�Ի���
     *
     * @param ctx ������ ����
     * @param iconId ͼ�꣬�磺R.drawable.icon ����
     * @param title ���� ����
     * @param itemsId �ַ���������Դid ����
     * @param listener ��ѡ��ť�����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
     * @param btnName ��ť���� ����
     * @param listener2 ��ť����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
     * @return
     */
    public static Dialog createRadioDialog(Context ctx,
                                           int iconId,
                                           String title,
                                           int itemsId,
                                           DialogInterface.OnClickListener listener,
                                           String btnName,
                                           DialogInterface.OnClickListener listener2) {
        Dialog dialog=null;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        // ���öԻ����ͼ��
        builder.setIcon(iconId);
        // ���öԻ���ı���
        builder.setTitle(title);
        // 0: Ĭ�ϵ�һ����ѡ��ť��ѡ��
        builder.setSingleChoiceItems(itemsId, 0, listener);
        // ���һ����ť
        builder.setPositiveButton(btnName, listener2) ;
        // ����һ����ѡ��ť�Ի���
        dialog = builder.create();
        return dialog;
    }


    /**
     * ������ѡ�Ի���
     *
     * @param ctx ������ ����
     * @param iconId ͼ�꣬�磺R.drawable.icon ����
     * @param title ���� ����
     * @param itemsId �ַ���������Դid ����
     * @param flags ��ʼ��ѡ��� ����
     * @param listener ��ѡ��ť�����������ʵ��android.content.DialogInterface.OnMultiChoiceClickListener�ӿ� ����
     * @param btnName ��ť���� ����
     * @param listener2 ��ť����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
     * @return
     */
    public static Dialog createCheckBoxDialog(Context ctx,
                                              int iconId,
                                              String title,
                                              int itemsId,
                                              boolean[] flags,
                                              android.content.DialogInterface.OnMultiChoiceClickListener listener,
                                              String btnName,
                                              DialogInterface.OnClickListener listener2) {
        Dialog dialog=null;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        // ���öԻ����ͼ��
        builder.setIcon(iconId);
        // ���öԻ���ı���
        builder.setTitle(title);
        builder.setMultiChoiceItems(itemsId, flags, listener);
        // ���һ����ť
        builder.setPositiveButton(btnName, listener2) ;
        // ����һ����ѡ�Ի���
        dialog = builder.create();
        return dialog;
    }


}
