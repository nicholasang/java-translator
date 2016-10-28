package inputs.testABCD;

class B extends A
{
    private String privateDataB = "dataB";

    @Override
    public String getArbitraryData() {
        return this.privateDataB;
    }

}
