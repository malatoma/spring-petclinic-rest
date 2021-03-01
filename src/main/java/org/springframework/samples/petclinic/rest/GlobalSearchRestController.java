package org.springframework.samples.petclinic.rest;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api/search")
public class GlobalSearchRestController 
{
		@Autowired
		private ClinicService clinicService;
		
	 	@PreAuthorize(  "hasRole(@roles.OWNER_ADMIN)" )
		@RequestMapping(value = "/{value}", method= RequestMethod.GET, produces= "application/json")
		public ResponseEntity<Collection<Owner>> getSearchResult(@PathVariable("value") String value)
		{
			if(value == null)
			{
				value="";
			}
			if (value!="")
			{
				Collection<Owner> allOwner = this.clinicService.findAllOwners();
				Collection<Owner> owners = new ArrayList<>() ;
				
				if (!allOwner.isEmpty())
				{
					for (Owner owner : allOwner)
					{
						if(value.contains(owner.getFirstName())||
									value.contains(owner.getLastName()))
						{
							owners.add(owner);
						}
						for(Pet pet : owner.getPets())
						{
							if( value.contains(pet.getName()) || value.equals(pet.getName()))
							{
								owners.add(owner);
							}
						}
					  }
				return new ResponseEntity<Collection<Owner>>(owners,HttpStatus.OK);
				}	
			}
			return new ResponseEntity<Collection<Owner>>(HttpStatus.NOT_FOUND);
		}
}
