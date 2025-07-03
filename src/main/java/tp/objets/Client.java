package tp.objets;

import org.bson.Document;

public class Client {
        private int idClient;
        private int age;
        private String prenom;
        private String nom;

        public Client() {}

        public Client(String prenom, String nom, int age) {
            this.prenom = prenom;
            this.nom = nom;
            this.age = age;
        }
        public Client(int idClient, String prenom, String nom, int age){
            this.idClient = idClient;
            this.prenom = prenom;
            this.nom = nom;
            this.age = age;
        }

        public Document toDocument() {
            Document doc = new Document()
                    .append("prenom", prenom)
                    .append("nom", nom)
                    .append("age", age);

            return doc;
        }

        // Getters et setters
        public String getNom() {
            return nom;
        }
        public String getPrenom() {
            return prenom;
        }
        public int getAge(){
            return age;
        }
        public int getIdClient(){
            return idClient;
        }

    }
}
