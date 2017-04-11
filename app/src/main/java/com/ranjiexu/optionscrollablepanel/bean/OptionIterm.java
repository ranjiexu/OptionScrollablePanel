package com.ranjiexu.optionscrollablepanel.bean;

/**
 * Created by devpc-05 on 2017/4/11.
 */

public class OptionIterm {
    //最新价
    private double LastPrice;
    //涨跌
    private double UpDown;
    //涨跌幅
    private double UpDownPercentage;
    //买价
    private double BuyPrice;
    //卖价
    private double SellPrice;
    //总量
    private double TotalAmount;
    //持仓量
    private double Positions;
    //仓差
    private double WarehouseDifference;
    //隐含波动率
    private double ImpliedVolatility;
    //期权理论价
    private double OptionTheoryPrice;
    //杠杆比率
    private double LeverageRate;
    //真实杠杆比率
    private double RealLeverageRate;
    //溢价率
    private double PremiumRate;
    //Delta
    private double Delta;
    //Gamma
    private double Gamma;
    //Rho
    private double Rho;
    //Theta
    private double Theta;
    //Vega
    private double Vega;

    public double getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(double lastPrice) {
        LastPrice = lastPrice;
    }

    public double getUpDown() {
        return UpDown;
    }

    public void setUpDown(double upDown) {
        UpDown = upDown;
    }

    public double getUpDownPercentage() {
        return UpDownPercentage;
    }

    public void setUpDownPercentage(double upDownPercentage) {
        UpDownPercentage = upDownPercentage;
    }

    public double getBuyPrice() {
        return BuyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        BuyPrice = buyPrice;
    }

    public double getSellPrice() {
        return SellPrice;
    }

    public void setSellPrice(double sellPrice) {
        SellPrice = sellPrice;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getPositions() {
        return Positions;
    }

    public void setPositions(double positions) {
        Positions = positions;
    }

    public double getWarehouseDifference() {
        return WarehouseDifference;
    }

    public void setWarehouseDifference(double warehouseDifference) {
        WarehouseDifference = warehouseDifference;
    }

    public double getImpliedVolatility() {
        return ImpliedVolatility;
    }

    public void setImpliedVolatility(double impliedVolatility) {
        ImpliedVolatility = impliedVolatility;
    }

    public double getOptionTheoryPrice() {
        return OptionTheoryPrice;
    }

    public void setOptionTheoryPrice(double optionTheoryPrice) {
        OptionTheoryPrice = optionTheoryPrice;
    }

    public double getLeverageRate() {
        return LeverageRate;
    }

    public void setLeverageRate(double leverageRate) {
        LeverageRate = leverageRate;
    }

    public double getRealLeverageRate() {
        return RealLeverageRate;
    }

    public void setRealLeverageRate(double realLeverageRate) {
        RealLeverageRate = realLeverageRate;
    }

    public double getPremiumRate() {
        return PremiumRate;
    }

    public void setPremiumRate(double premiumRate) {
        PremiumRate = premiumRate;
    }

    public double getDelta() {
        return Delta;
    }

    public void setDelta(double delta) {
        Delta = delta;
    }

    public double getGamma() {
        return Gamma;
    }

    public void setGamma(double gamma) {
        Gamma = gamma;
    }

    public double getRho() {
        return Rho;
    }

    public void setRho(double rho) {
        Rho = rho;
    }

    public double getTheta() {
        return Theta;
    }

    public void setTheta(double theta) {
        Theta = theta;
    }

    public double getVega() {
        return Vega;
    }

    public void setVega(double vega) {
        Vega = vega;
    }
}
