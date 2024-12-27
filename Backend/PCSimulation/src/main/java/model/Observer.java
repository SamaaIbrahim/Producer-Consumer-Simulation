package model;

public interface Observer {
    void addSubject(Subject subject);
    void update();
}
