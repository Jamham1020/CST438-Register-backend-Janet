package com.cst438.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {
	@Autowired
	StudentRepository studentRepository;
	
	/* Story 1: As an administrator, I can add a student to the system. 
	 * I input the student email and name.  
	 * The student email must not already exists in the system.
	 */
	@PostMapping("/student/add/{email}/{name}")
	@Transactional
	public StudentDTO addNewStudent(@PathVariable String email, @PathVariable String name) {
		// Check system using email to see if student already exists
		Student newStudent = studentRepository.findByEmail(email);
		
		// If the email already exists, throw an error message
		if(newStudent != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with that email already exists: " + email);
		}
		// If student email does not exist, create a new student
		else {
			newStudent = new Student();
			newStudent.setName(name);
			newStudent.setEmail(email);
			newStudent = studentRepository.save(newStudent);
			
			//DTO to display to server
			StudentDTO student = createStudentDTO(newStudent);
			
			return student;
		}
	}
	
	/* Story 2: As an administrator, I can put student registration on HOLD
	 * Status Code: 0 - No Hold
	 * 				1 - Hold
	*/
	@PutMapping("/student/addhold/{email}")
	@Transactional
	public StudentDTO addHoldtoStudent(@PathVariable String email) {
		// find student to place hold on their email
		Student student = studentRepository.findByEmail(email);
		
		// if email does not exist in the system, throw an error message
		if(student == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with that email does not exist:" + email);
		}
		// if email do exists in the system, update the status and status code to 1(HOLD)
		else {
			student.setStatusCode(1);
			student.setStatus("Hold");
			studentRepository.save(student);
			
			//DTO to display to server
			StudentDTO studentDTO = createStudentDTO(student);
			return studentDTO;
		}
	}
	
	/* Story 3: As an administrator, I can release the HOLD on student registration.
	 * Status Code: 0 - No Hold
	 * 				1 - Hold
	*/
	@PutMapping("/student/releasehold/{email}")
	@Transactional
	public StudentDTO releaseHold(@PathVariable String email) {
		// find student to release the hold placed on their email
		Student student = studentRepository.findByEmail(email);
		
		// if email does not exist in the system, throw an error message
		if(student == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student is not registered: " + email);
		}
		// if email do exist in the system, update the status and status code to NULL and 0
		student.setStatusCode(0);
		student.setStatus(null);
		studentRepository.save(student);
		
		// DTO to display to server
		StudentDTO studentDTO = createStudentDTO(student);
		return studentDTO;
	}
	
	//Helper method
	private StudentDTO createStudentDTO(Student student) {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.student_id = student.getStudent_id();
		studentDTO.name = student.getName();
		studentDTO.email = student.getEmail();
		studentDTO.statusCode = student.getStatusCode();
		studentDTO.status = student.getStatus();
		return studentDTO;
	}
	
}
