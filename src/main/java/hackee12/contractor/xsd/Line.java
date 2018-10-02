package hackee12.contractor.xsd;

public final class Line {

    private final String smartPath;
    private final String toString;

    private String getSmartPath() {
        return smartPath;
    }

    @Override
    public String toString() {
        return toString;
    }

    private Line(Builder builder) {
        this.smartPath = builder.computeSmartPath();
        this.toString = builder.computeToString();
    }

    static final class Builder {
        private String parentPath;
        private String key;
        private Qualifier qualifier;
        private String type;
        private boolean required;

        Builder(String key, Qualifier qualifier) {
            this.key = key;
            this.qualifier = qualifier;
        }

        Builder parent(Line parent) {
            this.parentPath = parent.getSmartPath();
            return this;
        }

        Builder type(String type) {
            this.type = type;
            return this;
        }

        Builder required(boolean required) {
            this.required = required;
            return this;
        }

        Line build() {
            return new Line(this);
        }

        private String computeSmartPath() {
            StringBuilder sb = new StringBuilder();

            if (null != parentPath) {
                sb.append(parentPath);
                sb.append("/");
            }
            switch (qualifier) {
                case LIST:
                    sb.append(key).append("[]");
                    break;
                case ATTRIBUTE:
                    sb.append("@").append(key);
                    break;
                case ELEMENT:
                    sb.append(key);
                    break;
                case VALUE:
                    sb.append("<value>");
                    break;
                default:
                    throw new IllegalStateException();
            }
            if (required) {
                sb.append("(r)");
            }

            return sb.toString();
        }

        private String computeToString() {
            StringBuilder sb = new StringBuilder();

            sb.append("'").append(computeSmartPath()).append("',");
            sb.append("'").append(null == qualifier ? "" : qualifier).append("',");
            sb.append("'").append(null == type ? "" : type).append("',");
            sb.append("'").append(required ? "required" : "optional").append("',");

            return sb.toString();
        }
    }

    enum Qualifier {
        LIST, ATTRIBUTE, ELEMENT, VALUE
    }
}
