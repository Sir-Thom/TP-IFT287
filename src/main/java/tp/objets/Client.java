package tp.objets;

import org.bson.Document;

public class Client {
        private int idClient;
        private int age;
        private String prenom;
        private String nom;

        public Client() {}
        public Client(Document d) {
            this.idClient = d.getInteger("idClient");
            this.prenom = d.getString("prenom");
            this.nom = d.getString("nom");
            this.age = d.getInteger("age");

         }
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
                    .append("idClient", idClient)
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
        public void setId(int idClient) {
            this.idClient = idClient;
        }
        public String setNom(String nom) {
            this.nom = nom;
            return this.nom;
        }
        public String setPrenom(String prenom) {
            this.prenom = prenom;
            return this.prenom;
        }
        public int setAge(int age) {
            this.age = age;
            return this.age;
        }

        @Override
        public String toString() {
            return "Client{" +
                    "idClient=" + idClient +
                    ", prenom='" + prenom + '\'' +
                    ", nom='" + nom + '\'' +
                    ", age=" + age +
                    '}';
        }

    }

