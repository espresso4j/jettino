package io.github.espresso4j.jettino;

public class BaseAdapterOptions {

    private Integer port = 8080;

    private String address = "0.0.0.0";

    private Boolean join = true;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getJoin() {
        return join;
    }

    public void setJoin(Boolean join) {
        this.join = join;
    }

    public static class Builder {
        public static BaseAdapterOptions byDefault() {
            return new BaseAdapterOptions();
        }
    }
}
