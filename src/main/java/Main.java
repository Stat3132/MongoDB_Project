public class Main {
//    public static void main(String[] args) throws IOException {
//        peopleList = Files.list(Paths.get(PEOPLE_DATA))
//                .map(Path::getFileName)
//                .map(Path::toString)
//                .sorted((a, b) -> Integer.compare(
//                        Integer.parseInt(a.split("\\.")[0]),
//                        Integer.parseInt(b.split("\\.")[0])))
//                .collect(Collectors.toList());
//
//        if (Files.exists(Paths.get(PEOPLE_SERIALIZED))) {
//            try {
//                Files.walk(Paths.get(PEOPLE_SERIALIZED))
//                        .sorted(Comparator.reverseOrder())
//                        .map(Path::toFile)
//                        .forEach(File::delete);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Files.createDirectories(Paths.get(PEOPLE_SERIALIZED));
//
//        peopleListRead();
//    }
}

