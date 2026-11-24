package myapp.skills;

public interface Skill {
    boolean canHandle(String input); // checks whether a skill can handle the input
    String handleInput(String input); // actually does the handling
}
