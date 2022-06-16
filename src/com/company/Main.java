package com.company;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    private static final int SERVER_PORT=8277;

    public static void main(String[] args) {

        List<Apartament> listaApartamente=new ArrayList<>();

        //citire fisier txt
        String fisier = "/Users/claudiapistol/IdeaProjects/subiectIntretineri-vechi/intretinere_apartamente.txt";

        try(BufferedReader reader = new BufferedReader(new FileReader(fisier))) {
            String linie;
            while( (linie = reader.readLine()) != null ) {
                String[] elemente = linie.split(",");
                int numarApartament = Integer.parseInt(elemente[0]);
                int suprafata = Integer.parseInt(elemente[1]);
                int numarPersoane = Integer.parseInt(elemente[2]);

                Apartament apartament = new Apartament(numarApartament, suprafata, numarPersoane);
                listaApartamente.add(apartament);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        System.out.println("afisare apartamente");
        for(Apartament a: listaApartamente) {
            System.out.println(a);
        }

        int max=0;
        int nr=0;
        for(Apartament a: listaApartamente){
            if(max<a.getSuprafata()) {
                max = a.getSuprafata();
                nr=a.getNrApartament();
            }
        }
        System.out.println("Cerinta 1");
        System.out.println("Apartamentul cu suprafata maxima este: " + nr);

        System.out.println("Cerinta 2");

        int suma = 0;
        for(Apartament a:listaApartamente){
            suma = listaApartamente
                    .stream()
                    .collect(Collectors.summingInt(Apartament::getNrPersoane));
        }

        System.out.println("Numarul total de perosane din imobil este: " + suma);

        System.out.println("Cerinta 3");


        //citire JSON
        List<Factura> listaFacturi = new ArrayList<>();
        String cale = "/Users/claudiapistol/IdeaProjects/subiectIntretineri-vechi/intretinere_facturi.json";
        try(var f = new FileReader(cale)) {
            var fisierJson = new JSONArray(new JSONTokener(f));

            for(int i=0;i<fisierJson.length();i++) {
                var json = fisierJson.getJSONObject(i);
                Factura factura = new Factura(
                        json.getString("denumire"),
                        json.getString("repartizare"),
                        json.getInt("valoare")
                );
                listaFacturi.add(factura);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //scriere JSON
//        List<Apartament> apartamentList = new ArrayList<>();
        try(PrintWriter out = new PrintWriter("output.json")) {
            JSONObject rad = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            listaApartamente.forEach(apartament -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("numar", apartament.getNrApartament());
                jsonObject.put("suprafata", apartament.getSuprafata());
                jsonObject.put("numar_persoane", apartament.getNrPersoane());
                jsonArray.put(jsonObject);
            });
            rad.put("cheia-listaobiectelor", jsonArray);
            rad.write(out,8,0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } ;

        //scriere txt
        String fisierText = "outputText.txt";
        try (PrintWriter writer = new PrintWriter(new File(fisierText))) {
            writer.println("denumire,repartizare,valoare");
            for(Factura f:listaFacturi) {
                writer.println(f.getDenumire()+","+f.getRepartizare()+","+f.getValoare());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        for(Factura f:listaFacturi){
            System.out.println(f);
        }
        List<Integer> listaFacturiTotale = new ArrayList<>();
        List<String> listaFacturiTotaleDenumire = new ArrayList<>();
        for(Factura f:listaFacturi) {
            int totalFacturi = listaFacturi
                    .stream()
                    .filter(s->s.getRepartizare().equals(f.getRepartizare()))
                    .collect(Collectors.summingInt(Factura::getValoare));
            listaFacturiTotale.add(totalFacturi);
            listaFacturiTotaleDenumire.add(f.getRepartizare());
        }

        Set<Integer> set = new HashSet<>(listaFacturiTotale);
        Set<String> set2 = new HashSet<>(listaFacturiTotaleDenumire);
        listaFacturiTotale.clear();
        listaFacturiTotaleDenumire.clear();
        listaFacturiTotale.addAll(set);
        listaFacturiTotaleDenumire.addAll(set2);

        System.out.println("Valoarea totala a facturilor este:");
        for(int i=0;i< listaFacturiTotale.size();i++) {
            System.out.println(listaFacturiTotaleDenumire.get(i) + " are factura de " + listaFacturiTotale.get(i));
        }


        try(var serverSocket=new ServerSocket(SERVER_PORT);
            var socket=serverSocket.accept();
            var in=new ObjectInputStream(socket.getInputStream());
            var out=new ObjectOutputStream(socket.getOutputStream())){
            int nrAp=(Integer)in.readObject();
            var nume=listaApartamente.stream().filter(apartament -> apartament.getNrApartament()==nrAp).findFirst().orElse(null);
            out.writeObject(nume.getSuprafata());
        }
        catch (Exception ex){

        }
    }
}
