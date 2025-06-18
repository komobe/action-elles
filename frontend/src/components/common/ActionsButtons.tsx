import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faEdit,
  faTrash,
  faEye,
  faPlus,
  faEllipsis,
} from '@fortawesome/free-solid-svg-icons';
import { ReactNode } from 'react';

export type ActionType =
  | 'create'
  | 'view'
  | 'edit'
  | 'delete'
  | 'other';

interface Action {
  type: ActionType;
  onClick: (item: unknown) => void;
  title?: string;
  disabled?: boolean;
  hidden?: boolean;
  color?: string;
  icon?: IconDefinition;
}

export type ActionList = Action[];

interface ActionsButtonsProps {
  item: unknown;
  actions: ActionList;
  className?: string;
  children?: ReactNode;
}

const getActionIcon = (type: ActionType) => {
  switch (type) {
    case 'create':
      return faPlus;
    case 'view':
      return faEye;
    case 'edit':
      return faEdit;
    case 'delete':
      return faTrash;
    default:
      return faEllipsis;
  }
};

const getDefaultTitle = (type: ActionType) => {
  switch (type) {
    case 'create':
      return 'Créer';
    case 'view':
      return 'Voir';
    case 'edit':
      return 'Modifier';
    case 'delete':
      return 'Supprimer';
    default:
      return '';
  }
};

const getDefaultColor = (type: ActionType) => {
  switch (type) {
    case 'create':
      return 'text-green-600 hover:text-green-900 dark:text-green-400 dark:hover:text-green-300';
    case 'view':
      return 'text-blue-600 hover:text-blue-900 dark:text-blue-400 dark:hover:text-blue-300';
    case 'edit':
      return 'text-yellow-600 hover:text-yellow-900 dark:text-yellow-400 dark:hover:text-yellow-300';
    case 'delete':
      return 'text-red-600 hover:text-red-900 dark:text-red-400 dark:hover:text-red-300';
    default:
      return 'text-gray-600 hover:text-gray-900 dark:text-gray-400 dark:hover:text-gray-300';
  }
};

const ActionsButtons = ({ item, actions, className = '', children }: ActionsButtonsProps) => {
  return (
    <div className={`flex justify-center space-x-2 ${className}`}>
      {actions.map((action, index) => {
        if (action.hidden) return null;

        const icon = action.icon || getActionIcon(action.type);
        const title = action.title ?? getDefaultTitle(action.type);
        const color = action.color ?? getDefaultColor(action.type);

        return (
          <button
            key={`${action.type}-${index}`}
            onClick={() => action.onClick(item)}
            disabled={action.disabled}
            className={`${color} transition-colors duration-200 ${action.disabled ? 'opacity-50 cursor-not-allowed' : ''}`}
            title={title}
          >
            <FontAwesomeIcon icon={icon} className="w-4 h-4" />
          </button>
        );
      })}
      {children}
    </div>
  );
};

export default ActionsButtons; 