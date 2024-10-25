package org.example;

class Card {
    // Основные
    private String id;
    private float price;
    private float profitPerHourDelta;
    private int cooldownSeconds;
    private boolean isAvailable;
    private boolean isExpired;
    // Вторичные
    private String name;
    private String section;
    private int level;
    private int maxLevel;
    private float currentProfitPerHour;
    private float profitPerHour;
    private int totalCooldownSeconds;

    public float getPayback() {
        // Проверяем, чтобы profitPerHour был больше нуля, иначе возможна ошибка деления на ноль
        if (profitPerHour > 0) {
            return price / profitPerHourDelta;
        } else {
            return Float.MIN_VALUE;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getProfitPerHour() {
        return profitPerHour;
    }

    public void setProfitPerHour(float profitPerHour) {
        this.profitPerHour = profitPerHour;
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public void setCooldownSeconds(int cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public float getCurrentProfitPerHour() {
        return currentProfitPerHour;
    }

    public void setCurrentProfitPerHour(float currentProfitPerHour) {
        this.currentProfitPerHour = currentProfitPerHour;
    }

    public float getProfitPerHourDelta() {
        return profitPerHourDelta;
    }

    public void setProfitPerHourDelta(float profitPerHourDelta) {
        this.profitPerHourDelta = profitPerHourDelta;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public int getTotalCooldownSeconds() {
        return totalCooldownSeconds;
    }

    public void setTotalCooldownSeconds(int totalCooldownSeconds) {
        this.totalCooldownSeconds = totalCooldownSeconds;
    }
}
