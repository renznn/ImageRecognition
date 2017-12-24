package Util;

import org.xutils.common.Callback.CancelledException;

public interface HttpCallback {
	public void onSuccess(String result);
	public void onError(Throwable ex);
	public void onCancelled(CancelledException cex);
	public void onFinished();
}
