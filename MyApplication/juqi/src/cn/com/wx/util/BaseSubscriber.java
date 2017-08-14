package cn.com.wx.util;

import rx.Subscriber;

/**
 * Created by wuxiaojun on 2017/2/28.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onStart() {
        super.onStart();
        /*if (!JudgeNetWork.isNetAvailable(App.getApplication())) {
            ToastUtils.show(App.getApplication(), "请检查网络!");
            return;
        }*/
        // 显示进度条
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onCompleted() {
        // 关闭进度条
    }

    /***
     * 对返回结果进行预先处理
     *
     * @param <T>
     */
    /*public class HttpResponseFun<T> implements Func1<BaseResponse<T>, T> {
        @Override
        public T call(BaseResponse<T> tBaseResponse) {
            if (tBaseResponse.code != 200) {
                throw new ApiHttpResonseException(tBaseResponse.code, tBaseResponse.msg);
            }
            return tBaseResponse.data;
        }

    }*/


}
