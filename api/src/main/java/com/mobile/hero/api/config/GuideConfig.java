package com.mobile.hero.api.config;

public class GuideConfig {
    private long mGuideGap = 0l;
    private long mFirstDelay = 0l;

    private GuideConfig() {

    }

    public long getGuideGap() {
        return mGuideGap;
    }

    public void setGuideGap(long mGuideGap) {
        this.mGuideGap = mGuideGap;
    }

    public long getFirstDelay() {
        return mFirstDelay;
    }

    public void setFirstDelay(long mFirstDelay) {
        this.mFirstDelay = mFirstDelay;
    }

    public static Build newBuild() {
        return new Build();
    }

    public static class Build {
        private long gap;
        private long firstDelay;

        private Build() {

        }

        public Build gap(long gap) {
            this.gap = gap;
            return this;
        }

        public Build firstDelay(long delay) {
            this.firstDelay = delay;
            return this;
        }

        public GuideConfig build() {
            GuideConfig config = new GuideConfig();
            config.setGuideGap(this.gap);
            config.setFirstDelay(this.firstDelay);
            return config;
        }
    }
}
