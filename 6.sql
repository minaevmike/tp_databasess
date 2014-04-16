SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `forums` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `forums` ;

-- -----------------------------------------------------
-- Table `forums`.`user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `forums`.`user` (
		  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
		    `isAnonymous` TINYINT(1) NULL ,
			  `username` VARCHAR(64) NULL ,
			    `email` VARCHAR(128) NOT NULL ,
				  `about` TINYTEXT NULL ,
				    `name` VARCHAR(128) NULL ,
					  PRIMARY KEY (`id`) ,
					    UNIQUE INDEX `email_UNIQUE` (`email` ASC) )
	ENGINE = InnoDB;


	-- -----------------------------------------------------
	-- Table `forums`.`forum`
	-- -----------------------------------------------------
	CREATE  TABLE IF NOT EXISTS `forums`.`forum` (
			  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
			    `forumName` VARCHAR(255) NOT NULL ,
				  `shortForumName` VARCHAR(128) NOT NULL ,
				    `user_id` INT UNSIGNED NOT NULL ,
					  PRIMARY KEY (`id`) ,
					    INDEX `fk_forum_user_idx` (`user_id` ASC) ,
						  UNIQUE INDEX `shortForumName_UNIQUE` (`shortForumName` ASC) ,
						    CONSTRAINT `fk_forum_user`
							    FOREIGN KEY (`user_id` )
								    REFERENCES `forums`.`user` (`id` )
									    ON DELETE NO ACTION
										    ON UPDATE NO ACTION)
	ENGINE = InnoDB;


	-- -----------------------------------------------------
	-- Table `forums`.`thread`
	-- -----------------------------------------------------
	CREATE  TABLE IF NOT EXISTS `forums`.`thread` (
			  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
			    `title` VARCHAR(255) NOT NULL ,
				  `isClosed` TINYINT(1) NOT NULL ,
				    `date` DATETIME NOT NULL ,
					  `message` TEXT NOT NULL ,
					    `slug` VARCHAR(255) NOT NULL ,
						  `isDeleted` TINYINT(1) NULL ,
						    `user_id` INT UNSIGNED NOT NULL ,
							  `forum_id` INT UNSIGNED NOT NULL ,
							    `deleted` TINYINT(1) NULL DEFAULT False ,
								  PRIMARY KEY (`id`) ,
								    INDEX `fk_thread_user1_idx` (`user_id` ASC) ,
									  INDEX `fk_thread_forum1_idx` (`forum_id` ASC) ,
									    CONSTRAINT `fk_thread_user1`
										    FOREIGN KEY (`user_id` )
											    REFERENCES `forums`.`user` (`id` )
												    ON DELETE NO ACTION
													    ON UPDATE NO ACTION,
														  CONSTRAINT `fk_thread_forum1`
														      FOREIGN KEY (`forum_id` )
	    REFERENCES `forums`.`forum` (`id` )
	    ON DELETE NO ACTION
		    ON UPDATE NO ACTION)
			ENGINE = InnoDB;


			-- -----------------------------------------------------
			-- Table `forums`.`post`
			-- -----------------------------------------------------
			CREATE  TABLE IF NOT EXISTS `forums`.`post` (
					  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
					    `parent` INT NULL ,
						  `isApproved` TINYINT(1) NULL ,
						    `isHighlighted` TINYINT(1) NULL ,
							  `isEdited` TINYINT(1) NULL ,
							    `isSpam` TINYINT(1) NULL ,
								  `isDeleted` TINYINT(1) NULL ,
								    `date` DATETIME NOT NULL ,
									  `message` TEXT NOT NULL ,
									    `user_id` INT UNSIGNED NOT NULL ,
										  `thread_id` INT UNSIGNED NOT NULL ,
										    `forum` VARCHAR(128) NOT NULL ,
											  `removed` TINYINT(1) NOT NULL DEFAULT False ,
											    PRIMARY KEY (`id`) ,
												  INDEX `fk_post_user1_idx` (`user_id` ASC) ,
												    INDEX `fk_post_thread1_idx` (`thread_id` ASC) ,
													  CONSTRAINT `fk_post_user1`
													      FOREIGN KEY (`user_id` )
														      REFERENCES `forums`.`user` (`id` )
															      ON DELETE NO ACTION
			    ON UPDATE NO ACTION,
				  CONSTRAINT `fk_post_thread1`
				      FOREIGN KEY (`thread_id` )
	    REFERENCES `forums`.`thread` (`id` )
	    ON DELETE NO ACTION
		    ON UPDATE NO ACTION)
			ENGINE = InnoDB;


			-- -----------------------------------------------------
			-- Table `forums`.`follows`
			-- -----------------------------------------------------
			CREATE  TABLE IF NOT EXISTS `forums`.`follows` (
					  `idFollowers` INT UNSIGNED NOT NULL ,
					    `idFollowing` INT UNSIGNED NOT NULL ,
						  `deleted` TINYINT(1) NULL DEFAULT False ,
						    PRIMARY KEY (`idFollowers`, `idFollowing`) ,
							  INDEX `fk_user_has_user_user2_idx` (`idFollowing` ASC) ,
							    INDEX `fk_user_has_user_user1_idx` (`idFollowers` ASC) ,
								  CONSTRAINT `fk_user_has_user_user1`
								      FOREIGN KEY (`idFollowers` )
									      REFERENCES `forums`.`user` (`id` )
										      ON DELETE NO ACTION
											      ON UPDATE NO ACTION,
												    CONSTRAINT `fk_user_has_user_user2`
													    FOREIGN KEY (`idFollowing` )
														    REFERENCES `forums`.`user` (`id` )
															    ON DELETE NO ACTION
																    ON UPDATE NO ACTION)
			ENGINE = InnoDB;


			-- -----------------------------------------------------
			-- Table `forums`.`vote`
			-- -----------------------------------------------------
			CREATE  TABLE IF NOT EXISTS `forums`.`vote` (
					  `type` INT NOT NULL ,
					    `id` INT NOT NULL ,
						  `value` SMALLINT(6) NOT NULL )
	ENGINE = InnoDB;


	-- -----------------------------------------------------
	-- Table `forums`.`subscribe`
	-- -----------------------------------------------------
	CREATE  TABLE IF NOT EXISTS `forums`.`subscribe` (
			  `thread_id` INT UNSIGNED NOT NULL ,
			    `user_id` INT UNSIGNED NOT NULL ,
				  `deleted` TINYINT(1) NULL DEFAULT false ,
				    PRIMARY KEY (`thread_id`, `user_id`) ,
					  INDEX `fk_thread_has_user_user1_idx` (`user_id` ASC) ,
					    INDEX `fk_thread_has_user_thread1_idx` (`thread_id` ASC) ,
						  CONSTRAINT `fk_thread_has_user_thread1`
						      FOREIGN KEY (`thread_id` )
							      REFERENCES `forums`.`thread` (`id` )
								      ON DELETE NO ACTION
									      ON UPDATE NO ACTION,
										    CONSTRAINT `fk_thread_has_user_user1`
											    FOREIGN KEY (`user_id` )
												    REFERENCES `forums`.`user` (`id` )
													    ON DELETE NO ACTION
														    ON UPDATE NO ACTION)
	ENGINE = InnoDB;

	USE `forums` ;


	SET SQL_MODE=@OLD_SQL_MODE;
	SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
	SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

