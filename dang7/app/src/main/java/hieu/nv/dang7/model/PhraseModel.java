package hieu.nv.dang7.model;

public class PhraseModel {
    private int id;
    private String vietnamese;
    private String english;

    public PhraseModel(int id, String vietnamese, String english) {
        this.id = id;
        this.vietnamese = vietnamese;
        this.english = english;
    }

    public int getId() { return id; }
    public String getVietnamese() { return vietnamese; }
    public String getEnglish() { return english; }
}