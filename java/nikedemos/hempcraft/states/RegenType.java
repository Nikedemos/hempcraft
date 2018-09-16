package nikedemos.hempcraft.states;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public enum RegenType {
    FIERY(new TypeFiery());

    private final IRegenType type;

    RegenType(IRegenType type) {
        this.type = type;
    }

    public IRegenType getType() {
        return type;
    }
}
