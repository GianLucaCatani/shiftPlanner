package com.shiftplanner.dao.fs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Employee;
import com.shiftplanner.model.EmployeeRole;

public class EmployeeDAOFileSystem implements EmployeeDAO {
	
	//Il nome del file che verrà creato nella root del tuo progetto
	private static final String FILE_PATH = "data/employees.csv";
	
	@Override 
	public List<Employee> getAllEmployees() throws DAOException {
		List<Employee> employees = new ArrayList<>();
		
		//Leggiamo il file di testo riga per riga
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
			String line;
			boolean isFirstLine = true;
			
			while ((line = br.readLine()) != null) {
				// Saltiamo la prima riga perché contiene solo le intestazioni (id, nome, ecc.)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Dividiamo la riga ad ogni virgola
                String[] data = line.split(",");
                if (data.length == 4) {
                    long id = Long.parseLong(data[0].trim());
                    String name = data[1].trim();
                    EmployeeRole role = EmployeeRole.valueOf(data[2].trim().toUpperCase());
                    int hours = Integer.parseInt(data[3].trim());
                    
                    // Ricreiamo l'oggetto Employee
                    employees.add(new Employee(id, name, role, hours));
                }
            }
        } catch (IOException e) {
            // Raccogliamo le eccezioni standard di Java e lanciamo la nostra eccezione Custom (Pattern corretto!)
            throw new DAOException("Errore nella lettura del file " + FILE_PATH, e);
        }
        
        return employees;
    }
    @Override
    public Employee getEmployeeById(long id) throws DAOException {
        List<Employee> allEmployees = getAllEmployees();
        for (Employee emp : allEmployees) {
            if (emp.getEmployeeId() == id) {
                return emp;
            }
        }
        return null;
    }
    
    @Override
    public void saveEmployee(Employee employee) throws DAOException {
        // 'true' nel FileWriter indica che vogliamo aggiungere (append) alla fine del file, senza cancellarlo
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            
            // Creiamo la riga separata da virgole
            String csvLine = employee.getEmployeeId() + "," +
                             employee.getFullName() + "," +
                             employee.getRole().name() + "," +
                             employee.getContractWeeklyHours();
                             
            pw.println(csvLine); // Scriviamo la riga nel file
            
        } catch (IOException e) {
            throw new DAOException("Errore durante il salvataggio nel file " + FILE_PATH, e);
        }
    }  
}
