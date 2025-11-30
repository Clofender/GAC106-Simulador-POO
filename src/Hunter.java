import java.util.Iterator;
import java.util.List;




public class Hunter implements Actor {
    // ---------------------------------------------------------
    private Location location;
    private Location homeLocation;
    private int kills;
    private boolean active;
    private boolean alive;

    // ---------------------------------------------------------
    // Construtor
    // ---------------------------------------------------------
    public Hunter(Location home) {
        this.homeLocation = home;
        this.location = home;
        this.alive = true;
        this.kills = 0;
        this.active = true; // pode ser alterado pelo clima/ciclo
    }

    // ---------------------------------------------------------
    // Métodos da interface Actor
    // ---------------------------------------------------------
    public void act(Field currentField, Field updatedField, List<Actor> newActors, WeatherSystem weather) {
        if(!alive) return;

    // INVERNO → não caça
    if (weather.getCurrentSeason() == Season.WINTER) {
        // regra comum: caçador volta pra casa
        returnHome(updatedField);
        return;
    }

         if(!active) {
        // Caçador inativo → retorna para casa
        returnHome(updatedField);
        return;
    }

        // Caçar
        goHunting(currentField, updatedField);
}
        /**
    * Retorna à localização inicial ("casa").
    */
        public void returnHome(Field updatedField) {
        location = homeLocation;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public int getKills() {
        return kills;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Lógica de caça: o hunter procura por um animal nas adjacências.
     */
    public void goHunting(Field currentField, Field updatedField) {
        Iterator<Location> adjacentLocs = currentField.adjacentLocations(location);
        while(adjacentLocs.hasNext()) {
            Location loc = adjacentLocs.next();
            Object obj = currentField.getObjectAt(loc);

            if(obj instanceof Actor prey && !(prey instanceof Animal)) {

                // matar a presa
                prey.setLocation(null); 
               
                kills++;

                // mover para o local da caça
                location = loc;
                return;
            }
        }

        // Se nada foi caçado, mover aleatoriamente
        Location newLoc = currentField.freeAdjacentLocation(location);
        if(newLoc != null) {
            location = newLoc;
        }
    }
}

