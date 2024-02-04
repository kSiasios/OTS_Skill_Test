import java.util.*;

public class Main {
    // Arrays used to randomly generate candidates and jobs
    public static String[] cities = {"Athens", "Thessaloniki", "Patra", "Volos", "Larissa"};
    public static String[] names = {"Nikolas", "Maria", "Konstantinos", "Ioanna", "Katerina"};
    public static String[] categories = {"Web", "Software", "Marketing", "DevOps", "Data Analysis"};
    public static String[] jobNames = {"Software Developer", "Fullstack Web Developer", "Marketing Consultant", "DevOps Developer", "Data Analyst"};

    // Colors for terminal colorization
    public static String reset = "\u001B[0m";
    public static String red = "\u001B[31m";
    public static String green = "\u001B[32m";
    public static String yellow = "\u001B[33m";
    public static String blue = "\u001B[34m";
    public static String purple = "\u001B[35m";
    public static String cyan = "\u001B[36m";

    // Driver function
    public static void main(String[] args) {
        // Declarations
        Random random = new Random();

        // Create a list of candidates for testing the functionality.
        List<Candidate> candidates = new ArrayList<>();
        candidates.add(new Candidate(getRandomFromArray(names), getRandomFromArray(cities), getRandomFromArray(categories), random.nextInt(10 - 0 + 1),true,false,"",""));
        candidates.add(new Candidate(getRandomFromArray(names), getRandomFromArray(cities), getRandomFromArray(categories), random.nextInt(10 - 0 + 1),true,true,"",""));
        candidates.add(new Candidate(getRandomFromArray(names), getRandomFromArray(cities), getRandomFromArray(categories), random.nextInt(10 - 0 + 1),false,false,"",""));
        candidates.add(new Candidate(getRandomFromArray(names), getRandomFromArray(cities), getRandomFromArray(categories), random.nextInt(10 - 0 + 1),false,true,"",""));
        candidates.add(new Candidate(getRandomFromArray(names), getRandomFromArray(cities), getRandomFromArray(categories), random.nextInt(10 - 0 + 1),true,false,"",""));

        // Create a list of jobs for testing the functionality.
        List<JobPosition> jobs = new ArrayList<>();
        jobs.add(new JobPosition(getRandomFromArray(jobNames), getRandomFromArray(cities), getRandomFromArray(categories), random.nextInt(10 - 0 + 1),true,true,true, new Date(),"","OTS",""));
        jobs.add(new JobPosition(getRandomFromArray(jobNames), getRandomFromArray(cities), getRandomFromArray(categories), random.nextInt(10 - 0 + 1),true,false,false, new Date(),"","OTS",""));
        jobs.add(new JobPosition(getRandomFromArray(jobNames), getRandomFromArray(cities), getRandomFromArray(categories), random.nextInt(10 - 0 + 1),false,false,true, new Date(),"","OTS",""));
        jobs.add(new JobPosition(getRandomFromArray(jobNames), getRandomFromArray(cities), getRandomFromArray(categories), random.nextInt(10 - 0 + 1),false,true,true, new Date(),"","OTS",""));
        jobs.add(new JobPosition(getRandomFromArray(jobNames), getRandomFromArray(cities), getRandomFromArray(categories), random.nextInt(10 - 0 + 1),true,false,true, new Date(),"","OTS",""));

        System.out.println(purple + "-----------------   AVAILABLE POSITIONS   -----------------" + reset);
        for (JobPosition job : jobs) {
            job.print();
        }

        // Express interest from a random candidate for a random job
        candidates.get(3).expressInterest(jobs.get(2));

        for (int i = 0; i < candidates.size(); i++) {
            Candidate current = candidates.get(i);
            List<JobPosition> list = getRecommendedPositions(current, jobs);
            current.print();
            for (JobPosition element : list) {
                element.print();
            }
        }

        // Activate second job and retry
        jobs.get(1).isActive = true;
        System.out.println(purple + "-----------------   AVAILABLE POSITIONS   -----------------" + reset);
        for (JobPosition job : jobs) {
            job.print();
        }
        for (int i = 0; i < candidates.size(); i++) {
            Candidate current = candidates.get(i);
            List<JobPosition> list = getRecommendedPositions(current, jobs);
            current.print();
            for (JobPosition element : list) {
                element.print();
            }
        }
    }

    public static String getRandomFromArray(String[] array) {
        // Create a Random object
        Random random = new Random();

        // Generate a random index within the range of the array length
        int randomIndex = random.nextInt(array.length);

        // Return the randomly selected name
        return array[randomIndex];
    }

    public static List<JobPosition> getRecommendedPositions(Candidate candidate, List<JobPosition> jobPostings) {
        List<JobPosition> stage1 = new ArrayList<JobPosition>();

        for (JobPosition job : jobPostings) {
            if (job.category == candidate.category) {
                // we have a matching category, add job to stage1 array
                stage1.add(job);
            }
        }

        int[] similarities = new int[stage1.size()];

        for (int i = 0; i < stage1.size(); i++) {
            JobPosition current = stage1.get(i);
            // check for actual similarities
            if (current.city == candidate.city) { similarities[i]++; }
            if (current.yearsOfExperience <= candidate.yearsOfExperience) { similarities[i]++; }
            if (!current.drivingLicense || current.drivingLicense == candidate.drivingLicense) { similarities[i]++; }
            if (!current.businessTravelling || current.businessTravelling == candidate.businessTravelling) { similarities[i]++; }
            // check if the candidate has expressed interest for this job
            if (candidate.getInterestedPositions() != null) {
                if (candidate.getInterestedPositions().contains(current)){ similarities[i]++; }
            }
        }

        sort(similarities, stage1);

        return stage1;
    }

    // A function that sorts a list according to the values of an array
    // used to sort the recommended jobs for a candidate
    // Selection Sort Algorithm
    public static void sort(int arr[], List<JobPosition> list) {

        int n = arr.length;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (arr[j] > arr[min_idx])
                    min_idx = j;

            // Swap the found minimum element with the first
            // element
            int temp = arr[min_idx];
            arr[min_idx] = arr[i];
            arr[i] = temp;

            JobPosition tempJob = list.get(min_idx);
            list.set(min_idx, list.get(i));
            list.set(i, tempJob);
        }
    }
}

class JobPosition extends Entity {
    public boolean isActive;
    public String minimumRequirements;
    public Date postingDate;
    public String company;
    public String positionDescription;

    // Constructor
    public JobPosition(String name, String city, String category, int yearsOfExperience, boolean drivingLicense, boolean businessTravelling, boolean isActive, Date postingDate, String minimumRequirements, String company, String positionDescription) {
        super(name, city, category, yearsOfExperience, drivingLicense, businessTravelling);
        this.isActive = isActive;
        this.postingDate = postingDate;
        this.minimumRequirements = minimumRequirements;
        this.company = company;
        this.positionDescription = positionDescription;
    }

    // A function that handles the deactivation of a job
    public void deactivate() {
        this.isActive = false;
    }

    public void print() {
        System.out.println(red);
        System.out.println("--------- JOB INFO ---------");
        System.out.println("--- NAME: \t\t\t\t\t\t" + this.name);
        System.out.println("--- COMPANY: \t\t\t\t\t" + this.company);
        System.out.println("--- CITY: \t\t\t\t\t\t" + this.city);
        System.out.println("--- CATEGORY: \t\t\t\t\t" + this.category);
        System.out.println("--- DESCRIPTION: \t\t\t\t" + this.positionDescription);
        System.out.println("--- YEARS OF EXPERIENCE: \t\t" + this.yearsOfExperience);
        System.out.println("--- NEED FOR DRIVING LICENSE: \t" + this.drivingLicense);
        System.out.println("--- NEED FOR BUSINESS TRIPS: \t" + this.businessTravelling);
        System.out.println("--- IS ACTIVE: \t\t\t\t\t" + this.isActive);
        System.out.println("--- DATE POSTED: \t\t\t\t" + this.postingDate);
        System.out.println("--- MINIMUM REQUIREMENTS: \t\t" + this.minimumRequirements);
        System.out.println(reset);
    }
}

class Candidate extends Entity {
    public String resume;
    public String demographics;
    public List<JobPosition> getInterestedPositions() {
        return interestedPositions;
    }

    private List<JobPosition> interestedPositions = new ArrayList<>();

    // Constructor
    public Candidate(String name, String city, String category, int yearsOfExperience, boolean drivingLicense, boolean businessTravelling, String resume, String demographics) {
        super(name, city, category, yearsOfExperience, drivingLicense, businessTravelling);
        this.resume = resume;
        this.demographics = demographics;
    }


    public void expressInterest(List<JobPosition> jobPositions) {
        for (int i = 0; i < jobPositions.size(); i++) {
            JobPosition jP = jobPositions.get(i);
            express(jP);
        }
    }
    public void expressInterest(JobPosition jobPosition) {
        express(jobPosition);
    }

    // A function for a candidate to express interest for a specified job
    private void express(JobPosition job) {
        interestedPositions.add(job);
        System.out.println(yellow + "********    Candidate" + reset);
        this.print();
        System.out.println(yellow + "********    expressed their interest for the following job" + reset);
        job.print();
    }

    public void print() {
        System.out.println(cyan);
        System.out.println("--------- CANDIDATE ---------");
        System.out.println("--- NAME: \t\t\t\t\t\t" + this.name);
        System.out.println("--- CITY: \t\t\t\t\t\t" + this.city);
        System.out.println("--- CATEGORY: \t\t\t\t\t" + this.category);
        System.out.println("--- DEMOGRAPHICS: \t\t\t\t" + this.demographics);
        System.out.println("--- YEARS OF EXPERIENCE: \t\t" + this.yearsOfExperience);
        System.out.println("--- HAS DRIVING LICENSE: \t\t" + this.drivingLicense);
        System.out.println("--- HAS BUSINESS TRIPS: \t\t" + this.businessTravelling);
        System.out.println("--- RESUME: \t\t\t\t\t" + this.resume);
        System.out.println(reset);
    }
}

// An entity that saves data similar for all other entities
class Entity {
    public static String reset = "\u001B[0m";
    public static String red = "\u001B[31m";
    public static String green = "\u001B[32m";
    public static String yellow = "\u001B[33m";
    public static String blue = "\u001B[34m";
    public static String purple = "\u001B[35m";
    public static String cyan = "\u001B[36m";

    public String name;
    public String city;
    public String category;
    public int yearsOfExperience;
    public boolean drivingLicense;
    public boolean businessTravelling;

    // Constructor for the Entity
    public Entity(String name, String city, String category, int yearsOfExperience, boolean drivingLicense, boolean businessTravelling) {
        this.name = name;
        this.city = city;
        this.category = category;
        this.yearsOfExperience = yearsOfExperience;
        this.drivingLicense = drivingLicense;
        this.businessTravelling = businessTravelling;
    }
}
