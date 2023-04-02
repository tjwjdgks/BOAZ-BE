package boaz.site.boazback.common.domain;

public enum FormEnum {
    //pass:1001 , fault:1002, error:1003
    PASS(1001)
    ,FAULT(1002)
    ,ERROR(1003)
    ,EXPIRE(1004);


    private int value;

    FormEnum(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

}
