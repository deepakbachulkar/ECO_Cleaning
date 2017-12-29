package com.ipws.eco.model;

/**
 * Created by  on 22/04/15.
 */
public class BaseVO {
    private String errorMessage = null;
    private Object requestTag;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getRequestTag() {
        return requestTag;
    }

    public void setRequestTag(Object requestTag) {
        this.requestTag = requestTag;
    }

//    private PopupDataVO popupDataVO;
//
//    public PopupDataVO getPopupDataVO() {
//        return popupDataVO;
//    }
//
//    public void setPopupDataVO(PopupDataVO popupDataVO) {
//        this.popupDataVO = popupDataVO;
//    }
}
