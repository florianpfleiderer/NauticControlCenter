package controlcenter;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import domain.CargoContainer;
import domain.ICargo;
import domain.Owner;
import domain.harbor.Harbor;
import domain.ship.ContainerShip;
import domain.ship.IShip;
import domain.ship.SmallContainerShip;
import utils.RandomNumberGenerator;

public class NauticControlCentre {

	private List<ContainerShip<ICargo>> ships = new ArrayList<>();
	private List<Harbor<ICargo>> harbors = new ArrayList<>();
	private List<Owner> owners = new ArrayList<>();
	private boolean setupDone = false;

	public void setup(int maxHarbors, int maxShips, int maxCargoInHarbor, int maxOwners, int maxObserved) {
		generateHarbors(maxHarbors);
		generateShips(maxShips);
		generateOwners(maxOwners, maxObserved);
		generateCargoInHarbors(maxCargoInHarbor);
		setupDone = true;
	}

	public void simulate() throws Exception {
		if (setupDone == false)
			throw new Exception();

		System.out.println(getSetupStatistic());

		System.out.println("___ START SIMULATION ____");
		int shipCount = ships.size();
		int harborsCount = harbors.size();

		long round = 0;

		// Using streams for a compact count of cargo containers that need
		// to be transfered - Streams are no concept explained in the VU OOP
		// and are only used for a compact solution!
		// Counts all containers in harbors and on ships that have to be
		// transported to their destination location.
		// BEGIN_STREAM_BLOCK : Java-Streams are _NOT_ part of the lecture!
		long harborCargoCount = harbors.parallelStream().filter(h -> h.isStorageSpaceEmpty() == false).count();
		long shipCargoCount = ships.parallelStream().filter(h -> h.isStorageSpaceEmpty() == false).count();
		// END_STREAM_BLOCK

		// Simulate until all cargo containers have been transported to their
		// destination
		while (harborCargoCount + shipCargoCount > 0) {

			round = round + 1;

			System.out.println("");
			System.out.println("Round [" + round + "]   -- Harbors with cargo: " + harborCargoCount
					+ " | Ships with cargo: " + shipCargoCount);

			// select ship to perform an action with
			IShip<ICargo> ship = ships.get(RandomNumberGenerator.INSTANCE.generateRandom(0, shipCount));
			// select a destionaion harbor for the ship
			Harbor<?> destination = harbors.get(RandomNumberGenerator.INSTANCE.generateRandom(0, harborsCount));

			// travel ship to destionation
			ship.travelTo(destination);

			// Counts all containers in harbors and on ships that have to be
			// transported to their destination location.
			// BEGIN_STREAM_BLOCK : Java-Streams are _NOT_ part of the lecture!
			harborCargoCount = harbors.parallelStream().filter(h -> h.isStorageSpaceEmpty() == false).count();
			shipCargoCount = ships.parallelStream().filter(s -> s.isStorageSpaceEmpty() == false).count();
			// END_STREAM_BLOCK

		}

		// clear all for new setup
		clearState();
	}

	public static void main(String[] args) {

		NauticControlCentre nauticController = new NauticControlCentre();

		nauticController.setup(10, 10, 30, 30, 3);
		try {
			nauticController.simulate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateHarbors(int maxHarbors) {

		long harborNumber = RandomNumberGenerator.INSTANCE.generateRandom(2, maxHarbors);

		for (int i = 0; i < harborNumber; i++) {
			harbors.add(new Harbor<ICargo>(i));
		}

	}

	private void generateShips(int maxShips) {
		int harborNumber = harbors.size();
		// If no harbors exist ships can not be placed in a harbor
		if (harborNumber == 0) {
			return;
		}

		int shipNumber = RandomNumberGenerator.INSTANCE.generateRandom(1, maxShips);

		for (int i = 0; i < shipNumber; i++) {

			Harbor<?> selectedHarbor = harbors.get(RandomNumberGenerator.INSTANCE.generateRandom(0, harborNumber));
			// int shipStorageSpace =
			// RandomNumberGenerator.INSTANCE.generateRandom(0, harborNumber);

			int shipType = RandomNumberGenerator.INSTANCE.generateRandom(0, 3);
			ContainerShip<ICargo> ship = null;
			switch (shipType) {
			case 0:
			case 1:
				ship = new ContainerShip<ICargo>(i, selectedHarbor);
				break;
			default:
				ship = new SmallContainerShip(i, selectedHarbor);
				break;

			}

			ships.add(ship);
		}
	}

	private void generateCargoInHarbors(int maxCargoInHarbor) {
		int harborNumber = harbors.size();
		// If no harbors exist no cargo can be stored
		if (harborNumber == 0) {
			return;
		}

		// generate unique id's for cargo
		long currentCargoId = 0;

		// Generate random number of cargo units in every harbor
		for (Harbor<ICargo> harbor : harbors) {
			int cargoNumber = RandomNumberGenerator.INSTANCE.generateRandom(0, maxCargoInHarbor);
			for (int i = 0; i < cargoNumber; i++) {

				Harbor<?> destination = harbors.get(RandomNumberGenerator.INSTANCE.generateRandom(1, harborNumber));
				CargoContainer<Harbor<?>> container = new CargoContainer<>(currentCargoId);
				container.setDestination(destination);
				harbor.addCargo(container);
				currentCargoId++;
			}
		}

	}

	private void generateOwners(int maxOwners, int maxObserved) {

		long ownerId = 0;

		for (Harbor<ICargo> harbor : harbors) {

			// determine how many owners should be created for each harbor and
			// how many ISubjects they observe
			// int ownerCount = RandomNumberGenerator.INSTANCE.generateRandom(1,
			// maxOwners);
			// int observerCount =
			// RandomNumberGenerator.INSTANCE.generateRandom(1, maxObserved);

			harbor.registerObserver(new Owner(ownerId));

			// Increment id
			ownerId = ownerId + 1;

		}

		for (IShip<ICargo> ship : ships) {

			// determine how many owners should be created for each harbor and
			// how many ISubjects they observe
			// int ownerCount = RandomNumberGenerator.INSTANCE.generateRandom(1,
			// maxOwners);
			// int observerCount =
			// RandomNumberGenerator.INSTANCE.generateRandom(1, maxObserved);

			ship.registerObserver(new Owner("Captain Nemo " + ownerId));

			// Increment id
			ownerId = ownerId + 1;

		}

	}

	private void clearState() {
		ships.clear();
		harbors.clear();
		owners.clear();
		setupDone = false;
	}

	private StringWriter getSetupStatistic() {
		StringWriter writer = new StringWriter();
		writer.append("___ CURRENT SETUP ____" + System.lineSeparator());
		writer.append("Harbors: " + harbors.size() + System.lineSeparator());
		writer.append("Ships: " + ships.size() + System.lineSeparator());
		writer.append("Owners: " + owners.size() + System.lineSeparator());
		for (Harbor<?> harbor : harbors) {
			writer.append(harbor.toString() + System.lineSeparator());
		}
		for (IShip<ICargo> ship : ships) {
			writer.append(ship.toString() + System.lineSeparator());
		}

		for (Owner owner : owners) {
			writer.append(owner.toString() + System.lineSeparator());
		}
		writer.flush();
		return writer;
	}

}
