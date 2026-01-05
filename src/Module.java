/**
 * Abstract base class for all learning modules in the Korean Learning App.
 * Provides common structure and methods for module implementations.
 * 
 * Creator: Group 3 Benny Java
 * Purpose: Define the contract and common behavior for all learning modules.
 */

public abstract class Module implements Learnable {

    protected String moduleName;
    protected String moduleDescription;

    /**
     * Constructor to initialize module with name and description.
     * @param name The name of the module
     * @param description A brief description of the module
     */
    public Module(String name, String description) {
        this.moduleName = name;
        this.moduleDescription = description;
    }

    /**
     * Get the module name.
     * @return Module name
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Get the module description.
     * @return Module description
     */
    public String getModuleDescription() {
        return moduleDescription;
    }

    /**
     * Abstract method to display the module menu.
     * Subclasses must provide their own implementation.
     */
    @Override
    public abstract void showLessonMenu();

    /**
     * Abstract method to display a specific lesson.
     * @param lessonNumber The lesson number to display
     */
    @Override
    public abstract void showLesson(int lessonNumber);

    /**
     * Override toString to provide a readable module representation.
     * @return String representation of the module
     */
    @Override
    public String toString() {
        return moduleName + " - " + moduleDescription;
    }
}
