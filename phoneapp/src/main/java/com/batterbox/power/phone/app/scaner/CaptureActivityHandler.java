//package com.batterbox.power.phone.app.scaner;
//
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
//import com.batterbox.power.phone.app.R;
//import com.batterbox.power.phone.app.act.ScanActivity;
//import com.zbar.lib.CameraManager;
//
//
///**
// * Author: Vondear
// * 描述: 扫描消息转发
// */
//public final class CaptureActivityHandler extends Handler {
//
//    DecodeThread decodeThread = null;
//    ScanActivity activity = null;
//    private State state;
//
//    private enum State {
//        PREVIEW, SUCCESS, DONE
//    }
//
//    public CaptureActivityHandler(ScanActivity activity) {
//        this.activity = activity;
//        Log.e("chenzefeng", "1");
//        decodeThread = new DecodeThread(activity);
//        Log.e("chenzefeng", "2");
//        decodeThread.start();
//        state = State.SUCCESS;
//        CameraManager.get().startPreview();
//        restartPreviewAndDecode();
//    }
//
//    @Override
//    public void handleMessage(Message message) {
//        if (message.what == R.id.auto_focus) {
//            if (state == State.PREVIEW) {
//                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
//            }
//        } else if (message.what == R.id.restart_preview) {
//            restartPreviewAndDecode();
//
//        } else if (message.what == R.id.decode_succeeded) {
//            state = State.SUCCESS;
//            activity.handleDecode((String) message.obj);// 解析成功，回调
//
//        } else if (message.what == R.id.decode_failed) {
//            state = State.PREVIEW;
//            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
//                    R.id.decode);
//        }
//    }
//
//    public void quitSynchronously() {
//        state = State.DONE;
//        CameraManager.get().stopPreview();
//        removeMessages(R.id.decode_succeeded);
//        removeMessages(R.id.decode_failed);
//        removeMessages(R.id.decode);
//        removeMessages(R.id.auto_focus);
//    }
//
//    private void restartPreviewAndDecode() {
//        if (state == State.SUCCESS) {
//            state = State.PREVIEW;
//            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
//                    R.id.decode);
//            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
//        }
//    }
//
//}
