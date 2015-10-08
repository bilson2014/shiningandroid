package com.panfeng.shining.dawn.image;

import in.srain.cube.request.DefaultRequestProxy;
import in.srain.cube.request.FailData;
import in.srain.cube.request.IRequest;
import in.srain.cube.request.IRequestProxy;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestBase;
import in.srain.cube.request.RequestProxyFactory;
import android.content.res.Resources;
import android.text.TextUtils;

import com.panfeng.shinning.R;

/**
 * process request
 */
public class DemoRequestProxy extends DefaultRequestProxy implements RequestProxyFactory {

    private static DemoRequestProxy sInstance;

    public static DemoRequestProxy getInstance() {
        if (sInstance == null) {
            sInstance = new DemoRequestProxy();
        }
        return sInstance;
    }

    @Override
    public void onRequestFail(RequestBase request, FailData failData) {

//        int code = failData.getErrorType();
//
//        Resources resources = CubeDemoApplication.instance.getResources();
//
//        String message = null;
//
//        switch (code) {
//            case FailData.ERROR_INPUT:
//                message = resources.getString(R.string.demo_request_error_input);
//                break;
//            case FailData.ERROR_DATA_FORMAT:
//                message = resources.getString(R.string.demo_request_error_data_format);
//                break;
//            case FailData.ERROR_NETWORK:
//                message = resources.getString(R.string.demo_request_error_network);
//                break;
//            case FailData.ERROR_UNKNOWN:
//                message = resources.getString(R.string.demo_request_error_unknown);
//                break;
//            case FailData.ERROR_CUSTOMIZED:
//                message = failData.getData(String.class);
//                break;
//        }
//
//        if (!TextUtils.isEmpty(message)) {
//            ErrorMessageDataEvent event = new ErrorMessageDataEvent(request.getFailData(), request.getRequestData().getTag(), message);
//            EventCenter.getInstance().post(event);
//        }
    }

    @Override
    public JsonData processOriginDataFromServer(RequestBase request, JsonData data) {
        return super.processOriginDataFromServer(request, data);
    }

    @Override
    public IRequestProxy createProxyForRequest(IRequest request) {
        return getInstance();
    }
}
