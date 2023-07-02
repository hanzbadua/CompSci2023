/**
 * Custom exception for failed level loads
 * We are inheriting RuntimeException as opposed to Exception as you must inherit RuntimeException for 
 * unchecked exceptions (exceptions that don't need to be caught or handled via throws statement)
 */
public class LevelLoadException extends RuntimeException
{
    public LevelLoadException(String msg)
    {
        super(msg);
    }
}
