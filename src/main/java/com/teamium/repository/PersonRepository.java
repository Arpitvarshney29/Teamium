package com.teamium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamium.domain.Person;

@Repository
public interface PersonRepository<T> extends JpaRepository<Person, Long> {

	/**
	 * To get person by discriminator
	 * 
	 * @param discriminator
	 * 
	 * @return List<Person>.
	 */
	@Query("SELECT p from Person p WHERE TYPE(p) =:discriminator")
	List<Person> getPersonsByDiscriminator(@Param("discriminator") Class<?> discriminator);

	/**
	 * To get person by id
	 * 
	 * @param discriminator
	 * @param id
	 * 
	 * @return Person.
	 */
	@Query("SELECT p from Person p WHERE TYPE(p) =:discriminator and p.id =:id")
	Person getPersonByDiscriminatorAndId(@Param("discriminator") Class<?> discriminator, @Param("id") Long id);

	/**
	 * To get staffEmials
	 * 
	 * @param email
	 * 
	 * @return StaffEmail.
	 */
	@Query("SELECT p FROM Person p join p.userSetting us join us.emails staffEmail WHERE :email IN(staffEmail.email)")
	List<Person> findByEmail(@Param("email") String email);

	/**
	 * To get staffTelephone
	 * 
	 * @param telephone
	 * 
	 * @return List<StaffTelephone>.
	 */
	@Query("SELECT p FROM Person p join p.userSetting us join us.telephones staffTelephone WHERE :telephone IN(staffTelephone.telephone)")
	List<Person> findByTelephone(@Param("telephone") String telephone);

}
