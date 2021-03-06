package info.xiancloud.plugin.scheduler.body_not_required;

import info.xiancloud.plugin.scheduler.IResponseDataOnly;

/**
 * 1、request body is not required
 * 2、response only the data in the {@link info.xiancloud.plugin.message.UnitResponse}
 *
 * @author happyyangyuan
 */
public class RequestBodyNotRequiredAndDataOnlyResponseAsyncForwarder extends AbstractBodyNotRequiredAsyncForwarder implements IJsonBody, IResponseDataOnly {

    public static final RequestBodyNotRequiredAndDataOnlyResponseAsyncForwarder singleton = new RequestBodyNotRequiredAndDataOnlyResponseAsyncForwarder();

}
